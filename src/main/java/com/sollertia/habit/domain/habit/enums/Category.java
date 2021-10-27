package com.sollertia.habit.domain.habit.enums;

public enum Category {
    A(""), B(""), C("");

    private String cate;

    Category(String cate) {
        this.cate = cate;
    }

    public String getString() {
        return this.cate;
    }

    public static Category fromString(String cates) {
        for (Category c : Category.values()) {
            if (c.cate.equals(cates)) {
                return c;
            }
        }
        return null;
    }

}
