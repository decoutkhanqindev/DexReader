#!/usr/bin/env python3
"""
Wraps all @Preview-annotated Compose functions with DexReaderTheme {}.

Usage (run from project root):
    python wrap_previews.py            # apply changes
    python wrap_previews.py --dry-run  # preview only, no writes
"""

import re
import sys
from pathlib import Path

BASE_DIR = Path(
    "app/src/main/java/com/decoutkhanqindev/dexreader/presentation/screens"
)
IMPORT_LINE = (
    "import com.decoutkhanqindev.dexreader.presentation.theme.DexReaderTheme"
)
DRY_RUN = "--dry-run" in sys.argv


# ---------------------------------------------------------------------------
# Brace matching (string / comment aware)
# ---------------------------------------------------------------------------

def find_matching_brace(text: str, pos: int) -> int:
    """
    Return the index of the closing '}' that matches the '{' at *pos*.
    Skips braces inside:
      - single-line comments  //
      - block comments        /* */
      - raw strings           \"\"\" ... \"\"\"
      - regular strings       " ... "
    Returns -1 if not found.
    """
    depth = 0
    i = pos
    n = len(text)

    while i < n:
        c = text[i]

        # Single-line comment
        if c == "/" and i + 1 < n and text[i + 1] == "/":
            while i < n and text[i] != "\n":
                i += 1
            continue

        # Block comment
        if c == "/" and i + 1 < n and text[i + 1] == "*":
            i += 2
            while i < n - 1 and not (text[i] == "*" and text[i + 1] == "/"):
                i += 1
            i += 2
            continue

        # Raw string  """..."""
        if c == '"' and text[i : i + 3] == '"""':
            i += 3
            while i < n and text[i : i + 3] != '"""':
                i += 1
            i += 3
            continue

        # Regular string  "..."
        if c == '"':
            i += 1
            while i < n:
                if text[i] == "\\":
                    i += 2
                    continue
                if text[i] == '"':
                    i += 1
                    break
                i += 1
            continue

        # Brace tracking
        if c == "{":
            depth += 1
        elif c == "}":
            depth -= 1
            if depth == 0:
                return i

        i += 1

    return -1


# ---------------------------------------------------------------------------
# Indentation helpers
# ---------------------------------------------------------------------------

def get_line_indent(text: str, pos: int) -> str:
    """Return the leading-whitespace string of the line containing *pos*."""
    line_start = text.rfind("\n", 0, pos) + 1
    line = text[line_start : pos + 1]
    return " " * (len(line) - len(line.lstrip()))


def indent_body(body: str) -> str:
    """Add 2 spaces to every non-blank line inside the function body."""
    lines = body.split("\n")
    return "\n".join("  " + l if l.strip() else l for l in lines)


# ---------------------------------------------------------------------------
# Core transformation
# ---------------------------------------------------------------------------

def build_wrapped(body: str, base_indent: str) -> str:
    """
    Produce the replacement text that goes between the function's opening '{'
    and its closing '}'.

    body        – original text between '{' and '}'
    base_indent – indentation of the 'fun' declaration line
    """
    inner = base_indent + "  "  # DexReaderTheme sits one level in

    lines = body.split("\n")

    if len(lines) <= 1:
        # Edge case: body on the same line as braces
        stripped = body.strip()
        if stripped:
            return (
                f"\n{inner}DexReaderTheme {{\n"
                f"{inner}  {stripped}\n"
                f"{inner}}}"
            )
        return f"\n{inner}DexReaderTheme {{\n{inner}}}"

    # Normal multi-line body:
    #   lines[0]  → '' (after the opening '{')
    #   lines[-1] → '' (before the closing '}')
    # lines[-1] is '' when the body ends with '\n' (standard); preserve that newline
    if lines[-1] == "":
        content_lines = lines[1:-1]
        trailing = "\n"
    else:
        content_lines = lines[1:]
        trailing = ""

    indented = ["  " + l if l.strip() else l for l in content_lines]

    return (
        f"\n{inner}DexReaderTheme {{\n"
        + "\n".join(indented)
        + f"\n{inner}}}{trailing}"
    )


# ---------------------------------------------------------------------------
# File processor
# ---------------------------------------------------------------------------

_PREVIEW_RE = re.compile(r"@Preview\b")
_FUN_RE = re.compile(r"\bfun\b")
# Tokens allowed between @Preview and fun: whitespace, annotations, modifiers
_ALLOWED_BETWEEN = re.compile(
    r"^[\s@\w\(\),=\"\.\-:]*$"
)


def process_file(path: Path) -> tuple[bool, int]:
    """
    Parse *path*, wrap every unwrapped @Preview function with DexReaderTheme,
    add the import if needed, and write the result (unless DRY_RUN).

    Returns (was_modified, number_of_previews_wrapped).
    """
    text = path.read_text(encoding="utf-8")

    if "@Preview" not in text:
        return False, 0

    # Collect (body_open, body_close) pairs, deduped by body_open
    modifications: dict[int, int] = {}

    for preview_match in _PREVIEW_RE.finditer(text):
        # Look for `fun` within the next ~300 chars (covers @Composable + modifiers)
        window = text[preview_match.start() : preview_match.start() + 300]
        fun_match = _FUN_RE.search(window)
        if not fun_match:
            continue

        # Verify only annotations / whitespace / modifiers between @Preview and fun
        between = window[: fun_match.start()]
        if not _ALLOWED_BETWEEN.match(between):
            continue

        abs_fun_start = preview_match.start() + fun_match.start()

        # Skip past `fun` keyword to find the opening '(' of the parameter list
        paren_open_pos = text.find("(", abs_fun_start + len("fun"))
        if paren_open_pos == -1:
            continue

        # Walk past the parameter list to find ')'
        depth = 0
        i = paren_open_pos
        while i < len(text):
            if text[i] == "(":
                depth += 1
            elif text[i] == ")":
                depth -= 1
                if depth == 0:
                    break
            i += 1
        paren_close_pos = i  # position of ')'

        # Find the function-body opening '{'
        body_open = text.find("{", paren_close_pos)
        if body_open == -1:
            continue

        # Sanity-check: '{' must be on the same line or the very next non-blank char
        between_paren_brace = text[paren_close_pos + 1 : body_open]
        if "\n\n" in between_paren_brace:
            # Two blank lines → likely not the function body
            continue

        body_close = find_matching_brace(text, body_open)
        if body_close == -1:
            continue

        # Skip if already wrapped
        if "DexReaderTheme" in text[body_open + 1 : body_close]:
            continue

        if body_open not in modifications:
            modifications[body_open] = body_close

    if not modifications:
        return False, 0

    # Apply end-to-start to preserve positions
    new_text = text
    for body_open, body_close in sorted(modifications.items(), reverse=True):
        base_indent = get_line_indent(new_text, body_open)
        body = new_text[body_open + 1 : body_close]
        replacement = build_wrapped(body, base_indent)
        new_text = new_text[: body_open + 1] + replacement + new_text[body_close:]

    # Add import if missing
    if IMPORT_LINE not in new_text:
        last_import = None
        for m in re.finditer(r"^import .+", new_text, re.MULTILINE):
            last_import = m
        if last_import:
            pos = last_import.end()
            new_text = new_text[:pos] + "\n" + IMPORT_LINE + new_text[pos:]
        else:
            pkg = re.search(r"^package .+", new_text, re.MULTILINE)
            if pkg:
                new_text = (
                    new_text[: pkg.end()] + "\n\n" + IMPORT_LINE + new_text[pkg.end() :]
                )

    if new_text == text:
        return False, 0

    if not DRY_RUN:
        path.write_text(new_text, encoding="utf-8")

    return True, len(modifications)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main() -> None:
    if not BASE_DIR.exists():
        print(f"ERROR: '{BASE_DIR}' not found. Run from the project root.")
        sys.exit(1)

    kt_files = sorted(BASE_DIR.rglob("*.kt"))
    total_files = 0
    total_previews = 0

    for f in kt_files:
        try:
            modified, count = process_file(f)
        except Exception as exc:
            rel = f.relative_to(BASE_DIR)
            print(f"  ERROR: {rel}: {exc}")
            continue

        if modified:
            total_files += 1
            total_previews += count
            rel = f.relative_to(BASE_DIR)
            prefix = "[DRY RUN] " if DRY_RUN else ""
            print(f"  {prefix}MODIFIED ({count} preview{'s' if count != 1 else ''}): {rel}")

    tag = "[DRY RUN] " if DRY_RUN else ""
    print(
        f"\n{tag}Done: {total_files} file{'s' if total_files != 1 else ''} modified, "
        f"{total_previews} preview{'s' if total_previews != 1 else ''} wrapped"
    )
    if DRY_RUN:
        print("  Run without --dry-run to apply changes.")


if __name__ == "__main__":
    main()
