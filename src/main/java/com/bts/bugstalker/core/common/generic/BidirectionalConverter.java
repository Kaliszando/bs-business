package com.bts.bugstalker.core.common.generic;

import java.util.List;

/**
 * Converter interface for mappings between objects,
 * using more complex logic than a simple mapper.
 *
 * @param <S> source class to convert from
 * @param <T> target class to convert to
 */
public interface BidirectionalConverter<S, T> extends Converter<S, T> {

    S convertBack(T target);

    default List<S> convertBack(List<T> sourceList) {
        return sourceList.stream()
                .map(this::convertBack)
                .toList();
    }
}
