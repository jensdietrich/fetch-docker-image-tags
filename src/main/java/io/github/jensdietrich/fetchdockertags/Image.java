package io.github.jensdietrich.fetchdockertags;

import java.util.Objects;

/**
 * Representation of an image.
 * @author jens dietrich
 */
public class Image {
    private String architecture = null;
    private String os = null;

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return Objects.equals(architecture, image.architecture) && Objects.equals(os, image.os);
    }

    @Override
    public int hashCode() {
        return Objects.hash(architecture, os);
    }

    @Override
    public String toString() {
        return "Image{" +
            "architecture='" + architecture + '\'' +
            ", os='" + os + '\'' +
            '}';
    }
}
