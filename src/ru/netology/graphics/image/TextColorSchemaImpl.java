package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    final static char[] C2 = {'▇', '●', '◉', '◍', '◎', '○', '☉', '◌', '-'};

    @Override
    public char convert(int color) {
        return C2[color / 32];
    }

}