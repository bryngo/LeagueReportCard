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
        final String APIKEY = "cda032a8-f14b-437d-a16d-e3941668ba5f";
        String summonerName = "ez3real";

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

            System.out.println(getSummonerMatchStats(match, summonerName));

        }

        System.out.println("Done!");

    }

    private static String getSummonerMatchStats(Match match, String summonerName) {

        List<Participant> participants = match.getParticipants();
        ParticipantStats summonerStats;
        String summonerChamp, matchSummary, date;

        date = match.getCreation().toString();

        int summonerID = 0;

        for(; summonerID < 10; summonerID++) {
            if(summonerName.equals(participants.get(summonerID).getSummonerName()))
                break;
        }

        long kills, assists, deaths;

        summonerChamp = participants.get(summonerID).getChampion().toString();

        summonerStats = participants.get(summonerID).getStats();

        kills = summonerStats.getKills();
        assists = summonerStats.getAssists();
        deaths = summonerStats.getDeaths();

        matchSummary = "Champ: " + summonerChamp + ". Got " +
                kills + " kills, " +
                assists + " assists, " +
                deaths + " deaths on " + date;


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