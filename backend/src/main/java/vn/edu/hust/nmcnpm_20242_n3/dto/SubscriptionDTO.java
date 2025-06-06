package vn.edu.hust.nmcnpm_20242_n3.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SubscriptionDTO implements Serializable {
        private Integer id;
        private String title;

        public SubscriptionDTO(Integer id, String title) {
            this.id = id;
            this.title = title;
        }

        // Getters and setters
    }

