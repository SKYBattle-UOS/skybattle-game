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
            case 2:
                property = makeZombieWannabe();
                break;
            case 3:
                property = makeVulture();
                break;
            case 4:
                property = makeHealthMan();
        }
        player.setProperty(property);
    }

    private PlayerProperty makeHealthMan() {
        PlayerProperty property = new PlayerProperty();
        List<Skill> skills = property.getSkills(friend);
        skills.add(_goFactory.createSkill(Util.HealthUpClassId));
        return property;
    }

    private PlayerProperty makeVulture() {
        PlayerProperty property = new PlayerProperty();
        List<Skill> skills = property.getSkills(friend);
        skills.add(_goFactory.createSkill(Util.SpiderMineClassId));
        return property;
    }

    private PlayerProperty makeZombieWannabe() {
        PlayerProperty property = new PlayerProperty();
        List<Skill> skills = property.getSkills(friend);
        skills.add(_goFactory.createSkill(Util.SuicideClassId));
        return property;
    }

    private PlayerProperty makeZombie() {
        PlayerProperty property = new PlayerProperty();
        property.setHealth(2000000);
        property.setMaxHealth(2000000);

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
            case 2:
                return "좀비 워너비";
            case 3:
                return "벌쳐";
            case 4:
                return "회복맨";
        }
        return "No Name";
    }

    public int size(){
        return getAvailableCharacterIndices().length;
    }

    public int[] getAvailableCharacterIndices(){
        return new int[]{ 2, 3, 4 };
    }
}
