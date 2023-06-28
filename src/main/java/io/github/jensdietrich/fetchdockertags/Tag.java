package io.github.jensdietrich.fetchdockertags;

import java.util.List;
import java.util.Objects;

/**
 * Representation of a tag.
 * @author jens dietrich
 */
public class Tag {
    private String name = null;
    private List<Image> images = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public boolean supportsOS(String osName) {
        for (Image image:images) {
            if (osName.equalsIgnoreCase(image.getOs())) {
                return true;
            }
        }
        return false;
    }

    public boolean supportsArchitecture(String architecture) {
        for (Image image:images) {
            if (architecture.equalsIgnoreCase(image.getArchitecture())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name) && Objects.equals(images, tag.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, images);
    }

    @Override
    public String toString() {
        return "Tag{" +
            "name='" + name + '\'' +
            '}';
    }
}
