package com.bts.bugstalker.common.converter;

import java.util.List;

/**
 * Converter interface for mappings between objects,
 * using more complex logic than a simple mapper.
 *
 * @param <S> source class to convert from
 * @param <T> target class to convert to
 */
public interface Converter<S, T> {

    T convert(S source);

    default List<T> convert(List<S> sourceList) {
        return sourceList.stream()
                .map(this::convert)
                .toList();
    }
}
