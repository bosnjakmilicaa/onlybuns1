package com.project.onlybuns.controller;

import com.project.onlybuns.model.Comment;
import com.project.onlybuns.model.Post;
import com.project.onlybuns.model.RegisteredUser;
import com.project.onlybuns.service.CommentService;
import com.project.onlybuns.service.PostService;
import com.project.onlybuns.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments") // Base path for comment-related endpoints
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, PostService postService) {
        this.commentService = commentService;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.findAll();
        return ResponseEntity.ok(comments);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

   /* @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment createdComment = commentService.save(comment);
        return ResponseEntity.ok(createdComment);
    } */

    @PostMapping("/create")
    @PreAuthorize("hasRole('REGISTERED')")
    public ResponseEntity<Map<String, Object>> createComment(
            @RequestParam("postId") Long postId,
            @RequestParam("content") String content) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        RegisteredUser loggedUser = userService.findByUsername1(username).orElse(null);

        if (loggedUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        long commentCountLastHour = commentService.countCommentsByUserInLastHour(loggedUser.getId());
        if (commentCountLastHour >= 60) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "You have exceeded the limit of 60 comments per hour."));
        }

        Post post = postService.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(loggedUser);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentService.save(comment);

        Map<String, Object> response = new HashMap<>();
        response.put("id", savedComment.getId());
        response.put("content", savedComment.getContent());
        response.put("username", loggedUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public Map<String, Long> getCommentStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("weekly", commentService.countCommentsForWeek());
        stats.put("monthly", commentService.countCommentsForMonth());
        stats.put("yearly", commentService.countCommentsForYear());
        return stats;
    }

}
