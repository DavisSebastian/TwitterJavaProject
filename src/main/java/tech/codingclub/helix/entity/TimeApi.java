package tech.codingclub.helix.entity;

public class TimeApi {

    private String time;
    private Long epoch_time;

    public TimeApi()
    {

    }

    public TimeApi(String time, Long epoch_time) {
        this.time = time;
        this.epoch_time = epoch_time;
    }
}
