package com.fluent.builder.domain;

import com.fluent.builder.infrastructure.secondary.ItemsToBuild;

public interface BuilderPort {
    void generateBuilder(ItemsToBuild itemsToBuild);
}
