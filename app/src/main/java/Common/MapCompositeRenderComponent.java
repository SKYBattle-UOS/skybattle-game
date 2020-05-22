package Common;

import com.example.Client.RenderComponent;

import java.util.ArrayList;

public class MapCompositeRenderComponent implements RenderComponent {
    private ArrayList<RenderComponent> _components = new ArrayList<>();

    @Override
    public void render(long ms) {
        for (RenderComponent c : _components)
            c.render(ms);
    }

    @Override
    public void destroy() {
        for (RenderComponent c : _components)
            c.destroy();
    }

    public void addRenderComponent(RenderComponent comp){
        _components.add(comp);
    }
}
