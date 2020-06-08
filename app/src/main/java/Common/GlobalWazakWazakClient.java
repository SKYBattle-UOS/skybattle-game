package Common;

import com.example.Client.CoordinateSkillClient;

public class GlobalWazakWazakClient extends CoordinateSkillClient {
    @Override
    public String getName() {
        return "원격 와작와작 뻥!";
    }

    @Override
    public void cast(GameObject caster) {
        runCoolTime(100);
    }
}
