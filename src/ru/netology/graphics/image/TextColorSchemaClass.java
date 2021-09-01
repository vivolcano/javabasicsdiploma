package ru.netology.graphics.image;

public class TextColorSchemaClass implements TextColorSchema {

    @Override
    public char convert(int color) {

        char[] c2 = {'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};
        return c2[color / 32];
    }

}