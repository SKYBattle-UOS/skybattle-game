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
                property = getPlayer0();
                break;
        }
        player.setProperty(property);
    }

    private PlayerProperty getPlayer0(){
        PlayerProperty property = new PlayerProperty(null);
        List<Skill> skills = property.getSkills(friend);
        skills.set(0, _goFactory.createSkill(Util.WazakWazakClassId));
        skills.set(1, _goFactory.createSkill(Util.GlobalWazakWazakClassId));
        skills.set(2, _goFactory.createSkill(Util.HealthUpClassId));
        skills.set(3, _goFactory.createSkill(Util.SuicideClassId));
        return property;
    }
}
