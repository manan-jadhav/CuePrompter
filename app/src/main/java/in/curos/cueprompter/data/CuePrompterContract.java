package in.curos.cueprompter.data;


import android.content.ContentResolver;
import android.content.Context;

import java.util.ArrayList;

/**
 * Created by curos on 6/11/16.
 */
public class CuePrompterContract {

    public static final String AUTHORITY = "in.curos.cueprompter";

    public static final class ScriptEntry {

        public static final String TABLE = "scripts";

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String CONTENT = "content";
        public static final String TIMESTAMP = "timestamp";

        public static final String TABLE_CREATE_QUERY =
                "create table "+ TABLE + " (" +
                        _ID + " integer primary key autoincrement, "+
                        TITLE + " text not null, "+
                        CONTENT + " text not null, "+
                        TIMESTAMP + " integer not null" +
                        ");";
    }

    public static void addDummyContent(Context context)
    {
        ArrayList<Script> scripts = dummyContent();
        ContentResolver contentResolver = context.getContentResolver();

        for (Script script : scripts) {
            contentResolver.insert(ScriptsProvider.SCRIPTS_BASE_URI, script.toContentValues());
        }
    }

    private static ArrayList<Script> dummyContent()
    {
        ArrayList<Script> scripts = new ArrayList<>();
        Script script = new Script();
        script.setTitle("I have a dream : Martin Luther King Jr.");
        script.setContent("I am happy to join with you today in what will go down in history as the greatest demonstration for freedom in the history of our nation.\n" +
                "\n" +
                "Five score years ago, a great American, in whose symbolic shadow we stand today, signed the Emancipation Proclamation. This momentous decree came as a great beacon light of hope to millions of Negro slaves who had been seared in the flames of withering injustice. It came as a joyous daybreak to end the long night of their captivity.\n" +
                "\n" +
                "But one hundred years later, the Negro still is not free. One hundred years later, the life of the Negro is still sadly crippled by the manacles of segregation and the chains of discrimination. One hundred years later, the Negro lives on a lonely island of poverty in the midst of a vast ocean of material prosperity. One hundred years later, the Negro is still languished in the corners of American society and finds himself an exile in his own land. And so we've come here today to dramatize a shameful condition.\n" +
                "\n" +
                "In a sense we've come to our nation's capital to cash a check. When the architects of our republic wrote the magnificent words of the Constitution and the Declaration of Independence, they were signing a promissory note to which every American was to fall heir. This note was a promise that all men, yes, black men as well as white men, would be guaranteed the \"unalienable Rights\" of \"Life, Liberty and the pursuit of Happiness.\" It is obvious today that America has defaulted on this promissory note, insofar as her citizens of color are concerned. Instead of honoring this sacred obligation, America has given the Negro people a bad check, a check which has come back marked \"insufficient funds.\"\n" +
                "\n" +
                "But we refuse to believe that the bank of justice is bankrupt. We refuse to believe that there are insufficient funds in the great vaults of opportunity of this nation. And so, we've come to cash this check, a check that will give us upon demand the riches of freedom and the security of justice.\n" +
                "\n" +
                "We have also come to this hallowed spot to remind America of the fierce urgency of Now. This is no time to engage in the luxury of cooling off or to take the tranquilizing drug of gradualism. Now is the time to make real the promises of democracy. Now is the time to rise from the dark and desolate valley of segregation to the sunlit path of racial justice. Now is the time to lift our nation from the quicksands of racial injustice to the solid rock of brotherhood. Now is the time to make justice a reality for all of God's children.\n" +
                "\n" +
                "It would be fatal for the nation to overlook the urgency of the moment. This sweltering summer of the Negro's legitimate discontent will not pass until there is an invigorating autumn of freedom and equality. Nineteen sixty-three is not an end, but a beginning. And those who hope that the Negro needed to blow off steam and will now be content will have a rude awakening if the nation returns to business as usual. And there will be neither rest nor tranquility in America until the Negro is granted his citizenship rights. The whirlwinds of revolt will continue to shake the foundations of our nation until the bright day of justice emerges.\n" +
                "\n" +
                "But there is something that I must say to my people, who stand on the warm threshold which leads into the palace of justice: In the process of gaining our rightful place, we must not be guilty of wrongful deeds. Let us not seek to satisfy our thirst for freedom by drinking from the cup of bitterness and hatred. We must forever conduct our struggle on the high plane of dignity and discipline. We must not allow our creative protest to degenerate into physical violence. Again and again, we must rise to the majestic heights of meeting physical force with soul force.\n" +
                "\n" +
                "The marvelous new militancy which has engulfed the Negro community must not lead us to a distrust of all white people, for many of our white brothers, as evidenced by their presence here today, have come to realize that their destiny is tied up with our destiny. And they have come to realize that their freedom is inextricably bound to our freedom.\n" +
                "\n");
        scripts.add(script);

        script = new Script();
        script.setTitle("Quit India Speech : Mahatma Gandhi");
        script.setContent("Before you discuss the resolution, let me place before you one or two things, I want you to understand two things very clearly and to consider them from the same point of view from which I am placing them before you. I ask you to consider it from my point of view, because if you approve of it, you will be enjoined to carry out all I say. It will be a great responsibility. There are people who ask me whether I am the same man that I was in 1920, or whether there has been any change in me. You are right in asking that question.\n" +
                "\n" +
                "Let me, however, hasten to assure that I am the same Gandhi as I was in 1920. I have not changed in any fundamental respect. I attach the same importance to non-violence that I did then. If at all, my emphasis on it has grown stronger. There is no real contradiction between the present resolution and my previous writings and utterances.\n" +
                "\n" +
                "Occasions like the present do not occur in everybody’s and but rarely in anybody’s life. I want you to know and feel that there is nothing but purest Ahimsa in all that I am saying and doing today. The draft resolution of the Working Committee is based on Ahimsa, the contemplated struggle similarly has its roots in Ahimsa. If, therefore, there is any among you who has lost faith in Ahimsa or is wearied of it, let him not vote for this resolution. Let me explain my position clearly. God has vouchsafed to me a priceless gift in the weapon of Ahimsa. I and my Ahimsa are on our trail today. If in the present crisis, when the earth is being scorched by the flames of Himsa and crying for deliverance, I failed to make use of the God given talent, God will not forgive me and I shall be judged unworthy of the great gift. I must act now. I may not hesitate and merely look on, when Russia and China are threatened.\n" +
                "\n" +
                "Ours is not a drive for power, but purely a non-violent fight for India’s independence. In a violent struggle, a successful general has been often known to effect a military coup and to set up a dictatorship. But under the Congress scheme of things, essentially non-violent as it is, there can be no room for dictatorship. A non-violent soldier of freedom will covet nothing for himself, he fights only for the freedom of his country. The Congress is unconcerned as to who will rule, when freedom is attained. The power, when it comes, will belong to the people of India, and it will be for them to decide to whom it placed in the entrusted. May be that the reins will be placed in the hands of the Parsis, for instance-as I would love to see happen-or they may be handed to some others whose names are not heard in the Congress today. It will not be for you then to object saying, “This community is microscopic. That party did not play its due part in the freedom’s struggle; why should it have all the power?” Ever since its inception the Congress has kept itself meticulously free of the communal taint. It has thought always in terms of the whole nation and has acted accordingly. . . I know how imperfect our Ahimsa is and how far away we are still from the ideal, but in Ahimsa there is no final failure or defeat. I have faith, therefore, that if, in spite of our shortcomings, the big thing does happen, it will be because God wanted to help us by crowning with success our silent, unremitting Sadhana for the last twenty-two years.\n" +
                "\n" +
                "I believe that in the history of the world, there has not been a more genuinely democratic struggle for freedom than ours. I read Carlyle’s French Revolution while I was in prison, and Pandit Jawaharlal has told me something about the Russian revolution. But it is my conviction that inasmuch as these struggles were fought with the weapon of violence they failed to realize the democratic ideal. In the democracy which I have envisaged, a democracy established by non-violence, there will be equal freedom for all. Everybody will be his own master. It is to join a struggle for such democracy that I invite you today. Once you realize this you will forget the differences between the Hindus and Muslims, and think of yourselves as Indians only, engaged in the common struggle for independence.\n" +
                "\n" +
                "Then, there is the question of your attitude towards the British. I have noticed that there is hatred towards the British among the people. The people say they are disgusted with their behaviour. The people make no distinction between British imperialism and the British people. To them, the two are one. This hatred would even make them welcome the Japanese. It is most dangerous. It means that they will exchange one slavery for another. We must get rid of this feeling. Our quarrel is not with the British people, we fight their imperialism. The proposal for the withdrawal of British power did not come out of anger. It came to enable India to play its due part at the present critical juncture It is not a happy position for a big country like India to be merely helping with money and material obtained willy-nilly from her while the United Nations are conducting the war. We cannot evoke the true spirit of sacrifice and velour, so long as we are not free. I know the British Government will not be able to withhold freedom from us, when we have made enough self-sacrifice. We must, therefore, purge ourselves of hatred. Speaking for myself, I can say that I have never felt any hatred. As a matter of fact, I feel myself to be a greater friend of the British now than ever before. One reason is that they are today in distress. My very friendship, therefore, demands that I should try to save them from their mistakes. As I view the situation, they are on the brink of an abyss. It, therefore, becomes my duty to warn them of their danger even though it may, for the time being, anger them to the point of cutting off the friendly hand that is stretched out to help them. People may laugh, nevertheless that is my claim. At a time when I may have to launch the biggest struggle of my life, I may not harbor hatred against anybody.");
        scripts.add(script);

        return scripts;
    }
}
