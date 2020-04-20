package com.company.framework.dtos;

public class ProductDto {
    private int _id;
    private String _name;
    private int _price;
    private int _stock;
    private int _length;
    private int _width;
    private int _height;

    public ProductDto(int id, String name, int price, int stock, int length, int width, int height) {
        _id = id;
        _name = name;
        _price = price;
        _stock = stock;
        _length = length;
        _width = width;
        _height = height;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public int get_price() {
        return _price;
    }

    public void set_price(int _price) {
        this._price = _price;
    }

    public int get_stock() {
        return _stock;
    }

    public void set_stock(int _stock) {
        this._stock = _stock;
    }

    public int get_length() {
        return _length;
    }

    public void set_length(int _length) {
        this._length = _length;
    }

    public int get_width() {
        return _width;
    }

    public void set_width(int _width) {
        this._width = _width;
    }

    public int get_height() {
        return _height;
    }

    public void set_height(int _height) {
        this._height = _height;
    }
}