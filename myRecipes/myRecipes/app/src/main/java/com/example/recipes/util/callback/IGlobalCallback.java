package com.example.recipes.util.callback;


import androidx.annotation.Nullable;

   
public interface IGlobalCallback<T> {

    void executeCallback(@Nullable T args);
}
