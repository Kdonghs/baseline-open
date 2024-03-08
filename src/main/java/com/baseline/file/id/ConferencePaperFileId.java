package com.baseline.file.id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter @Setter
@NoArgsConstructor
@Embeddable
public class ConferencePaperFileId implements Serializable {
    @Column(name = "conference_paper_id")
    private Integer conferencePaperId;

    @Column(name = "file_id")
    private Integer fileId;

    public ConferencePaperFileId(Integer conferencePaperId, Integer fileId) {
        this.conferencePaperId = conferencePaperId;
        this.fileId = fileId;
    }
}
