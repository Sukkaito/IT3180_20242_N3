package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vn.edu.hust.nmcnpm_20242_n3.constant.BookCopyStatusEnum;

import java.io.Serializable;

/**
 * DTO for {@link vn.edu.hust.nmcnpm_20242_n3.entity.BookCopy}
 */
@AllArgsConstructor
@Getter
public class BookCopyDTO implements Serializable {
    private final int id;
    private final int originalBookBookId;
    private final String status;
}