package com.github.highd120.editer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class EditerData {
    @Singular
    private List<ElementData> elements;
    @Singular
    private List<String> inventoryNames;

    public enum ElementType {
        NUMBER, CHECKER
    }

    @AllArgsConstructor
    @Value
    public static class ElementData {
        private ElementType type;
        private String name;
    }
}
