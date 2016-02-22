package com.nklmish.boundary;

public interface Converter<Model, DTO> {

    Model toModel(DTO model);

}