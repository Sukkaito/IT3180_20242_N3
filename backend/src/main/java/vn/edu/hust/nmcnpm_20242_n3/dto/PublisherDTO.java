package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.hust.nmcnpm_20242_n3.entity.Publisher}
 */
@AllArgsConstructor
@Getter
public class PublisherDTO implements Serializable {
    private final int id;
    private final String name;
}