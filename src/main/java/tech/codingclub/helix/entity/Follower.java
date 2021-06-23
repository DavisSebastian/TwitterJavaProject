package tech.codingclub.helix.entity;

public class Follower {
    public Long user_id;
    public Long following_id;

    public Long getUser_id() {
        return user_id;
    }

    public Long getFollowing_id() {
        return following_id;
    }

    public Follower(Long user_id, Long following_id) {
        this.user_id = user_id;
        this.following_id = following_id;
    }
}
