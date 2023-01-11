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

	public MarkdownTable() {
	}

	public MarkdownTable(List<String> header) {
		this.addCells(header);
		this.nextRow();
	}

	public void addCell(Object value) {
		currentRow.add(Objects.toString(value));
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
			throw new IllegalStateException("Building of the previous row is not finished. Call nextRow() first.");
		}
		addCells(values);
		nextRow();
	}

	public void addRow(Object... values) {
		if (!currentRow.isEmpty()) {
			throw new IllegalStateException("Building of the previous row is not finished. Call nextRow() first.");
		}
		addCells(values);
		nextRow();
	}

	public void nextRow() {
		if (header.isEmpty()) {
			header.addAll(currentRow);
		} else {
			if (currentRow.size() != header.size()) {
				throw new IllegalArgumentException("Current row size [" + currentRow.size() + "] should be equal to header size [" + header.size() + "].");
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
		List<Integer> widths = header.stream().map(String::length).collect(Collectors.toList());
		for (int i = 0; i < header.size(); i++) {
			for (List<String> row : rows) {
				widths.set(i, Math.max(row.get(i).length(), widths.get(i)));
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
		repeat(' ', size - value.length(), sb);
		sb.append(' ');
	}
}
