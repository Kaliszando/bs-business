package com.bts.bugstalker.util.generic;

import java.util.List;

/**
 * Converter interface for mappings between objects,
 * using more complex logic than a simple mapper.
 *
 * @param <S> source class to convert from
 * @param <T> target class to convert to
 */
public interface TwoWayConverter<S, T> extends Converter<S, T> {

    S reverseConvert(T target);

    default List<S> reverseConvert(List<T> sourceList) {
        return sourceList.stream()
                .map(this::reverseConvert)
                .toList();
    }
}
