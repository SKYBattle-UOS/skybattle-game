package Common;

import com.example.Client.GameObjectFactory;

import java.util.List;

public class CharacterFactory {
    public static class Friend {
        private Friend(){}
    }

    private static final Friend friend = new Friend();
    private GameObjectFactory _goFactory;

    public CharacterFactory(GameObjectFactory goFactory){
        _goFactory = goFactory;
    }

    public void setCharacterProperty(Player player, int index){
        PlayerProperty property = null;
        switch (index){
            case 0:
                property = makeTestChar();
                break;
            case 1:
                property = makeZombie();
                break;
        }
        player.setProperty(property);
    }

    private PlayerProperty makeZombie() {
        PlayerProperty property = new PlayerProperty();
        property.setHealth(99999000);
        property.setMaxHealth(99999000);

        List<Skill> skills = property.getSkills(friend);
        skills.add(_goFactory.createSkill(Util.SuicideClassId));
        return property;
    }

    private PlayerProperty makeTestChar(){
        PlayerProperty property = new PlayerProperty();
        List<Skill> skills = property.getSkills(friend);
        skills.add(_goFactory.createSkill(Util.GlobalWazakWazakClassId));
        skills.add(_goFactory.createSkill(Util.HealthUpClassId));
        skills.add(_goFactory.createSkill(Util.SuicideClassId));
        return property;
    }

    public String getCharacterName(int index){
        switch (index){
            case 0:
                return "테스트 캐릭터";
            case 1:
                return "좀비";
        }
        return "No Name";
    }

    public int size(){
        return getAvailableCharacterIndices().length;
    }

    public int[] getAvailableCharacterIndices(){
        return new int[]{ 0, 1 };
    }
}
