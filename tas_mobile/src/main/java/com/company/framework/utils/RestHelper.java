package com.company.framework.utils;

import com.company.framework.dtos.IssueDetailsDto;
import com.company.framework.dtos.IssueDto;
import org.joda.time.DateTime;

public class RestHelper {


    public IssueDto setIssueDetailsDto(int projectId, int trackerId, int statusId, int priorityId,
                                              String subject, String description) {

        IssueDto issueDto = new IssueDto();
        IssueDetailsDto issueDetailsDto = new IssueDetailsDto();

        issueDetailsDto.setProject_id(projectId);
        issueDetailsDto.setTracker_id(trackerId);
        issueDetailsDto.setStatus_id(statusId);
        issueDetailsDto.setPriority_id(priorityId);
        issueDetailsDto.setSubject(subject);
        issueDetailsDto.setDescription(description);

        issueDto.setIssue(issueDetailsDto);

        return issueDto;
    }

    public String getStringID() {

        DateTime dt = new DateTime();
        int month = dt.getDayOfMonth();
        int hours = dt.getHourOfDay();
        int min = dt.getMinuteOfHour();
        int number;

            if (min >= 0 && min <= 10)
        {
            number = 1;
        } else if (min > 10 && min <= 20) {
            number = 2;
        }
            else if (min > 20 && min <= 30) {
            number = 3;
        }
            else if (min > 30 && min <= 40) {
            number = 4;
        }
            else if (min > 40 && min <= 50) {
            number = 5;
        }
            else {
            number = 6;
        }

        return "_" + month+hours+number;
    }

}
