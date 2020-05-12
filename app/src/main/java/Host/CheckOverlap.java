package Host;
import android.util.Log;
import java.lang.Math;

public class CheckOverlap {
    /* 게임 오브젝트들이 일정 반지름(반경)km의 원 안에 있다. */
    float latitude, longitude; // 게임오브젝트의 (위도,경도)는 원의 중심이 됨.
    float radius;              // 게임오브젝트마다 반지름(반경)이 정해져 있으면 필요없음

    public CheckOverlap(float x, float y, float r) {
        latitude = x;
        longitude = y;
        radius = r;
    }

    public void checkOverlap(CheckOverlap a, CheckOverlap b) {
        double dist;
        //두 원의 중심사이의 거리
        dist = Math.sqrt(((a.latitude - b.latitude) * (a.latitude - b.latitude)) + ((a.longitude - b.longitude) * (a.longitude - b.longitude)));
        if (dist < a.radius + b.radius) {
            System.out.println("충돌!!!!"); //테스트용
            /* 플레이어 x 아이템 : 아이템 먹기 */
            /* 술래 x 러너 = HP 감소 (지속적) */
        }
        return;
    }

    public static void main(String[] args) { //테스트용 메인
        CheckOverlap c1 = new CheckOverlap(15, 0, 10);
        CheckOverlap c2 = new CheckOverlap(0, 0, 10);

        c1.checkOverlap(c1, c2);
    }
}