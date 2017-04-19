import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.match.Participant;
import com.robrua.orianna.type.core.match.ParticipantStats;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import com.robrua.orianna.type.core.summoner.Summoner;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

class Example {
    public static void main(String[] args) throws InterruptedException {


        // Sensitive information
        final String APIKEY = "";
        String summonerName = "";

        // Set the environment
        RiotAPI.setRegion(Region.NA);
        RiotAPI.setAPIKey(APIKEY);

        // Get the summoner of interest
        Summoner summoner = RiotAPI.getSummonerByName(summonerName);
        List<MatchReference> matchList = summoner.getMatchList();
        Match match;

        for (MatchReference aMatchList : matchList) {
            match = RiotAPI.getMatch(aMatchList.getID());

            // Don't really care about games from 2015 and beyond
            if(getMatchYear(match) < 2016) break;

            System.out.println(getSummonerMatchStats(match, summoner));

        }

        System.out.println("Done!");

    }



    private static boolean getMatchOutcome(Match match, int participantID) {

        int teamID;

        if(participantID >= 0 && participantID <= 4)
            teamID = 0;
        else teamID = 1;

        return match.getTeams().get(teamID).getWinner();
    }

    private static String getSummonerMatchStats(Match match, Summoner summoner) {

        List<Participant> participants = match.getParticipants();
        ParticipantStats summonerStats;
        String summonerChamp, matchSummary, date;
        boolean matchWon;

        date = match.getCreation().toString();

        int participantID = 0;

        for(; participantID < 10; participantID++) {
            if(summoner.getID() == participants.get(participantID).getSummonerID())
                break;
        }

        matchWon = getMatchOutcome(match, participantID);

        long kills, assists, deaths;

        summonerChamp = participants.get(participantID).getChampion().toString();

        summonerStats = participants.get(participantID).getStats();


        kills = summonerStats.getKills();
        assists = summonerStats.getAssists();
        deaths = summonerStats.getDeaths();

        matchSummary = "Champ: " + summonerChamp + ". Got " +
                kills + " kills, " +
                assists + " assists, " +
                deaths + " deaths on " + date;
        if(matchWon)
            matchSummary += " . Victory!";
        else
            matchSummary += " . Defeat :(";

        return matchSummary;
    }

    private static int getMatchYear(Match match) {

        // Method source:
        // http://stackoverflow.com/questions/9474121/i-want-to-get-year-month-day-etc-from-java-date-to-compare-with-gregorian-cal
        Date creation;
        Calendar cal = Calendar.getInstance();

        creation = match.getCreation();
        cal.setTime(creation);

        return cal.get(Calendar.YEAR);

    } // method to get the year from a match
}