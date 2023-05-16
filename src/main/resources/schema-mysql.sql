CREATE TABLE `review` (
                          `review_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                          `review_date` DATE NOT NULL,
                          `review_content` VARCHAR(1000) NOT NULL,
                          `member_id` BIGINT NOT NULL,
                          `reg_time` datetime(6) DEFAULT NULL,
                          `update_time` datetime(6) DEFAULT NULL,
                          `created_by` varchar(255) DEFAULT NULL,
                          `modified_by` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`review_id`),
                          FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
