package com.example.walkwalkrevolution.team;

import com.example.walkwalkrevolution.account.IAccountInfo;

import java.util.ArrayList;

public class TeamSubject implements ITeamSubject {
    ArrayList<IAccountInfo> teamMembers;
    TeamSubject() {teamMembers = new ArrayList<>();}
    public void update(ArrayList<IAccountInfo> newList) {
        teamMembers = newList;
    }
}
