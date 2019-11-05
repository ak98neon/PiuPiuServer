package com.unity.shooter.piupiu_server.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Rotation {
    private String x;
    private String y;
    private String z;
    private String w;

    public Rotation() {
    }

    @JsonCreator
    public Rotation(@JsonProperty("x") String x, @JsonProperty("y") String y, @JsonProperty("z") String z,
                    @JsonProperty("w") String w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    @Override
    public String toString() {
        return "Rotation{" +
                "x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", z='" + z + '\'' +
                ", w='" + w + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rotation rotation = (Rotation) o;
        return Objects.equals(x, rotation.x) &&
                Objects.equals(y, rotation.y) &&
                Objects.equals(z, rotation.z) &&
                Objects.equals(w, rotation.w);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
}
