#!/usr/bin/env python3
"""UI reuse checker: run it before building a new UI piece, to see whether other screens use the same piece.

The design HTML (for-agent/html/*.html) keeps every style inline, so elements that look the same share
the same CSS values. Two modes:

  Screen mode: list each styled element of one screen that also appears on other screens.
      python3 for-agent/tools/ui_reuse_check.py --screen 1-5
  Query mode: list the screens with an element whose inline style has ALL the given declarations.
      python3 for-agent/tools/ui_reuse_check.py "height:56px" "border-radius:28px" "background:#1B3A6B"

Colours may be written as #RRGGBB or rgb(r, g, b). A piece found on another screen (or on two places of the
same screen) belongs in ui/components/; check ui/components/ for an existing component first.
"""
import argparse
import collections
import glob
import html
import os
import re

HTML_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "html")
# Properties that define how a piece looks. Position, margin and size of the outer box are left out on purpose.
SIGNATURE_KEYS = ("height", "border-radius", "border", "background", "font-size", "font-weight", "padding")
ELEMENT = re.compile(r'<([a-zA-Z0-9]+)\b[^>]*?\bstyle="([^"]*)"[^>]*>([^<]{0,40})')


def norm_value(value: str) -> str:
    value = value.strip().lower()
    value = re.sub(r"#([0-9a-f]{6})\b", lambda m: "rgb({}, {}, {})".format(*(int(m.group(1)[i:i + 2], 16) for i in (0, 2, 4))), value)
    value = re.sub(r"\s*,\s*", ", ", value)
    return re.sub(r"\s+", " ", value)


def parse_style(style: str) -> dict:
    decls = {}
    for part in html.unescape(style).split(";"):
        if ":" in part:
            key, value = part.split(":", 1)
            decls[key.strip().lower()] = norm_value(value)
    return decls


def screens():
    for path in sorted(glob.glob(os.path.join(HTML_DIR, "*.html"))):
        name = os.path.basename(path)[:-5]          # e.g. 05_1-5_ProfileCreate
        yield name, [(tag, parse_style(style), text.strip()) for tag, style, text in ELEMENT.findall(open(path, encoding="utf-8").read())]


def signature(decls: dict):
    sig = tuple((k, decls[k]) for k in SIGNATURE_KEYS if k in decls)
    # Needs a visible box (background or border) and at least three defining values, so plain text is skipped.
    if len(sig) < 3 or not ({"background", "border"} & decls.keys()) or decls.get("background") == "transparent" and "border" not in decls:
        return None
    return sig


def screen_mode(screen_id: str):
    all_screens = list(screens())
    index = collections.defaultdict(set)
    for name, elements in all_screens:
        for _, decls, _ in elements:
            sig = signature(decls)
            if sig:
                index[sig].add(name)
    target = [s for s in all_screens if s[0].split("_")[1] == screen_id or s[0].startswith(screen_id)]
    if not target:
        raise SystemExit(f"No screen matches '{screen_id}' (use e.g. 1-5 or 05).")
    name, elements = target[0]
    print(f"Screen {name}: pieces also used elsewhere (→ build or reuse a component in ui/components/)\n")
    seen = collections.Counter(signature(d) for _, d, _ in elements)
    printed = set()
    for tag, decls, text in elements:
        sig = signature(decls)
        if not sig or sig in printed:
            continue
        others = sorted(index[sig] - {name})
        if others or seen[sig] > 1:
            printed.add(sig)
            where = ", ".join(o.split("_")[1] for o in others) or "—"
            print(f"<{tag}> \"{text[:24]}\"  ×{seen[sig]} here · other screens: {where}")
            print("    " + "; ".join(f"{k}:{v}" for k, v in sig))
    if not printed:
        print("Nothing on this screen repeats elsewhere.")


def query_mode(declarations):
    wanted = parse_style(";".join(declarations))
    hits = collections.defaultdict(list)
    for name, elements in screens():
        for tag, decls, text in elements:
            if all(decls.get(k) == v for k, v in wanted.items()):
                hits[name].append(text[:24])
    for name, texts in hits.items():
        print(f"{name}: {len(texts)} × " + " | ".join(f'"{t}"' for t in texts[:4]))
    print(f"\n{len(hits)} screen(s).")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--screen", help="screen id, e.g. 1-5 or 05")
    parser.add_argument("declaration", nargs="*", help='CSS declarations, e.g. "height:56px"')
    args = parser.parse_args()
    if args.screen:
        screen_mode(args.screen)
    elif args.declaration:
        query_mode(args.declaration)
    else:
        parser.print_help()
