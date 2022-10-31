package com.yareakh.keyring.repository;

import com.yareakh.keyring.model.Message;
import org.springframework.data.repository.CrudRepository;

/**
 * Use this CRU class to create messages
 */
public interface MessageRepository extends CrudRepository<Message, String> {
}
