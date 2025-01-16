
package com.project.onlybuns.repository;

import com.project.onlybuns.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatGroupId(Long groupId);
    List<Message> findTop10ByChatGroupIdOrderByTimestampDesc(Long chatGroupId);
    List<Message> findTop10ByGroupNameOrderByTimestampDesc(String groupName);


    @Query("SELECT m FROM Message m JOIN FETCH m.sender JOIN FETCH m.chatGroup WHERE m.chatGroup.name = :groupName")
    List<Message> findMessagesWithSenderByGroupName(@Param("groupName") String groupName);


}
