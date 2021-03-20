package models;

import lombok.Getter;

import java.util.List;

@Getter
public class Resp {
    private int cod;
    private String message;
    private long cnt;
    private List<Days> list;
    private models.City city;
}
