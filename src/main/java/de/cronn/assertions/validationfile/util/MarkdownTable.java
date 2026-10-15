package de.cronn.assertions.validationfile.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MarkdownTable {

  private final List<String> header = new ArrayList<>();

  private final List<List<String>> rows = new ArrayList<>();

  private List<String> currentRow = new ArrayList<>();

  public MarkdownTable() {}

  public MarkdownTable(List<String> header) {
    this.addCells(header);
    this.nextRow();
  }

  public void addCell(Object value) {
    currentRow.add(escape(Objects.toString(value)));
  }

  // an unescaped pipe or line break would end the cell or the row
  private static String escape(String value) {
    return value.replace("|", "\\|").replace("\r\n", "<br>").replace("\n", "<br>");
  }

  public void addCells(Collection<? extends Object> values) {
    values.forEach(this::addCell);
  }

  public void addCells(Object... values) {
    for (Object value : values) {
      addCell(value);
    }
  }

  public void addRow(Collection<? extends Object> values) {
    if (!currentRow.isEmpty()) {
      throw new IllegalStateException(
          "Building of the previous row is not finished. Call nextRow() first.");
    }
    addCells(values);
    nextRow();
  }

  public void addRow(Object... values) {
    if (!currentRow.isEmpty()) {
      throw new IllegalStateException(
          "Building of the previous row is not finished. Call nextRow() first.");
    }
    addCells(values);
    nextRow();
  }

  public void nextRow() {
    if (header.isEmpty()) {
      header.addAll(currentRow);
    } else {
      if (currentRow.size() != header.size()) {
        throw new IllegalArgumentException(
            "Current row size ["
                + currentRow.size()
                + "] should be equal to header size ["
                + header.size()
                + "].");
      }
      rows.add(currentRow);
    }
    currentRow = new ArrayList<>();
  }

  public String toString() {
    List<Integer> maxCellLengths = computeColumnsWidths();
    if (maxCellLengths.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    sb.append('|');
    for (int i = 0; i < header.size(); i++) {
      pad(header.get(i), maxCellLengths.get(i), sb);
      sb.append('|');
    }
    sb.append("\n|");
    for (int i = 0; i < header.size(); i++) {
      repeat('-', maxCellLengths.get(i) + 2, sb);
      sb.append('|');
    }
    for (List<String> row : rows) {
      sb.append("\n|");
      for (int i = 0; i < row.size(); i++) {
        pad(row.get(i), maxCellLengths.get(i), sb);
        sb.append('|');
      }
    }
    return sb.toString();
  }

  private List<Integer> computeColumnsWidths() {
    List<Integer> widths =
        header.stream().map(MarkdownTable::displayWidth).collect(Collectors.toList());
    for (int i = 0; i < header.size(); i++) {
      for (List<String> row : rows) {
        widths.set(i, Math.max(displayWidth(row.get(i)), widths.get(i)));
      }
    }
    return widths;
  }

  private void repeat(char ch, int times, StringBuilder sb) {
    for (int i = 0; i < times; i++) {
      sb.append(ch);
    }
  }

  private void pad(String value, int size, StringBuilder sb) {
    sb.append(' ');
    sb.append(value);
    repeat(' ', size - displayWidth(value), sb);
    sb.append(' ');
  }

  /**
   * Number of columns the value occupies in a monospace font: East Asian wide characters and emoji
   * take two columns, combining marks and format characters (such as the zero width joiner) none.
   */
  static int displayWidth(String value) {
    int width = 0;
    int i = 0;
    while (i < value.length()) {
      int codePoint = value.codePointAt(i);
      i += Character.charCount(codePoint);
      if (isZeroWidth(codePoint)) {
        continue;
      }
      boolean emojiPresentation =
          i < value.length() && value.charAt(i) == EMOJI_PRESENTATION_SELECTOR;
      width += isWide(codePoint) || emojiPresentation ? 2 : 1;
    }
    return width;
  }

  private static final char EMOJI_PRESENTATION_SELECTOR = '\uFE0F';

  private static boolean isZeroWidth(int codePoint) {
    int type = Character.getType(codePoint);
    return type == Character.NON_SPACING_MARK
        || type == Character.ENCLOSING_MARK
        || type == Character.FORMAT;
  }

  // approximates East_Asian_Width W and F, which the JDK does not provide
  private static boolean isWide(int codePoint) {
    if (Character.isEmojiPresentation(codePoint)) {
      return true;
    }
    return switch (Character.UnicodeScript.of(codePoint)) {
      case HAN, HIRAGANA, KATAKANA, HANGUL, BOPOMOFO, YI -> true;
      default ->
          Character.UnicodeBlock.of(codePoint) == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
              || (codePoint >= 0xFF01 && codePoint <= 0xFF60) // fullwidth forms
              || (codePoint >= 0xFFE0 && codePoint <= 0xFFE6);
    };
  }
}
