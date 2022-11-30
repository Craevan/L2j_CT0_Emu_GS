package com.l2j.emu.commons.data;

import com.l2j.emu.commons.lang.StringUtil;
import com.l2j.emu.commons.math.MathUtil;

import java.util.AbstractList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Pagination<A> extends AbstractList<A> {

    private static final String NORMAL_LINE_SIZE = "<img height=17>";

    private List<A> list;
    private int page;
    private int limit;
    private int total;

    public Pagination(Stream<A> stream, int page, int limit) {
        this(stream, page, limit, null, null);
    }

    public Pagination(Stream<A> stream, Predicate<A> filter) {
        this(stream, 0, 0, filter, null);
    }

    public Pagination(Stream<A> stream, int page, int limit, Predicate<A> filter) {
        this(stream, page, limit, filter, null);
    }

    public Pagination(Stream<A> stream, Comparator<A> comparator) {
        this(stream, 0, 0, null, comparator);
    }

    public Pagination(Stream<A> stream, int page, int limit, Comparator<A> comparator) {
        this(stream, page, limit, null, comparator);
    }

    public Pagination(Stream<A> stream, Predicate<A> filter, Comparator<A> comparator) {
        this(stream, 0, 0, filter, comparator);
    }

    public Pagination(Stream<A> stream, int page, int limit, Predicate<A> filter, Comparator<A> comparator) {
        this.list = initList(stream, filter, comparator);

        if (page <= 0 || limit <= 0 || list.isEmpty()) {
            return;
        }

        this.limit = limit;
        this.total = list.size() / limit + (list.size() % limit == 0 ? 0 : 1);
        this.page = MathUtil.limit(page, 1, total);
        this.list = list.subList((Math.min(page, total) - 1) * limit, Math.min(Math.min(page, this.total) * limit, this.list.size()));
    }

    private List<A> initList(Stream<A> stream, Predicate<A> filter, Comparator<A> comparator) {
        if (stream == null) {
            return Collections.emptyList();
        }

        if (filter == null && comparator == null) {
            return stream.collect(Collectors.toList());
        }

        if (comparator == null) {
            return stream.filter(filter).collect(Collectors.toList());
        }

        if (filter == null) {
            return stream.sorted(comparator).collect(Collectors.toList());
        }

        return stream.filter(filter).sorted(comparator).collect(Collectors.toList());
    }

    public void generateSpace(StringBuilder sb) {
        IntStream.range(size(), limit).forEach(x -> sb.append(NORMAL_LINE_SIZE));
    }

    public void generateSpace(StringBuilder sb, String content) {
        IntStream.range(size(), limit).forEach(x -> sb.append(content));
    }

    public void generatePages(StringBuilder sb, String action) {
        StringUtil.append(sb, "<table width=270 bgcolor=000000><tr><td width=30 align=center><button action=\"",
                action.replace("%page%", String.valueOf(1)),
                "\" back=L2UI_CH3.prev1_down fore=L2UI_CH3.prev1 width=16 height=16></td>");

        for (int index = page - 5; index < page - 1; index++)
            StringUtil.append(sb, "<td width=30>" + (index < 0 ? "" : "<a action=\"" + action.replace("%page%",
                    String.valueOf(index + 1)) + "\">" + (((index + 1) < 10 ? "0" : "") + (index + 1)) + "</a>") + "</td>");

        StringUtil.append(sb, "<td width=30><font color=LEVEL>",
                ((page < 10 ? "0" : "") + (page == 0 ? "1" : page)), "</font></td>");

        for (int index = page; index < page + 4; index++)
            StringUtil.append(sb, "<td width=30>" + (index < total ? "<a action=\"" + action.replace("%page%", String.valueOf(index + 1)) + "\">" + (((index + 1) < 10 ? "0" : "") + (index + 1)) + "</a>" : "") + "</td>");

        StringUtil.append(sb,
                "<td width=30 align=center><button action=\"",
                action.replace("%page%", String.valueOf(total)),
                "\" back=L2UI_CH3.next1_down fore=L2UI_CH3.next1 width=16 height=16></td></tr></table>");
    }

    @Override
    public A get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }
}
