import com.robrua.orianna.api.core.RiotAPI;
import com.robrua.orianna.type.core.common.Region;
import com.robrua.orianna.type.core.match.Match;
import com.robrua.orianna.type.core.match.Participant;
import com.robrua.orianna.type.core.match.ParticipantStats;
import com.robrua.orianna.type.core.matchlist.MatchReference;
import com.robrua.orianna.type.core.summoner.Summoner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class Example {
    public static void main(String[] args) throws InterruptedException {


        // Sensitive information
        final String APIKEY = getBryanAPIKey("League");
        ;
        String summonerName = "Lunoski";

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
    } // returns if the participant actually won the match

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


        // get the KDA
        kills = summonerStats.getKills();
        deaths = summonerStats.getDeaths();
        assists = summonerStats.getAssists();

        // construct the match summary string
        matchSummary = "Champ: " + summonerChamp + ". Got " +
                kills + " kills, " +
                assists + " assists, " +
                deaths + " deaths on " + date;
        if(matchWon)
            matchSummary += " . Victory!";
        else
            matchSummary += " . Defeat :(";

        // return the match summary;
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

    private static String getBryanAPIKey(String APIcompany) {

        // holds the list of api keys
        List<String> records = new ArrayList<String>();

        // open the file and add the keys to the list
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("/Users/Bryan1/keys.text"));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);
            }
            reader.close();
        }

        // error
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", "/Users/Bryan1/keys.text");
            e.printStackTrace();
            return null;
        }

        // return the requested api key
        for (String record : records)
            if (record.contains(APIcompany)) {
                String[] tokens = record.split("=");
                return tokens[1];
            }


        return "ERROR";

    }

} // returns an APIKey.