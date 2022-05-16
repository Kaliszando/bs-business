package com.bts.bugstalker.util.generic;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Converter interface for mappings between objects,
 * using more complex logic than a simple mapper.
 *
 * @param <S> source class to convert from
 * @param <D> destination class to convert to
 */
public interface Converter<S, D> {

    D convert(S source);

    default Collection<D> convert(Collection<S> sourceList) {
        return sourceList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
