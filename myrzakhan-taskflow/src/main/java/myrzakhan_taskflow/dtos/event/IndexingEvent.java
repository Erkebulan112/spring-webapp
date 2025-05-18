package myrzakhan_taskflow.dtos.event;

public sealed interface IndexingEvent permits CommentIndexEvent, TaskIndexEvent, ProjectIndexEvent,
        CommentIndexDelete, TaskIndexDelete, ProjectIndexDelete { }

