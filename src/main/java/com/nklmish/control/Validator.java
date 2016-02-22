package com.nklmish.control;

public interface Validator<T, V> {

    T validate(V param);

}
