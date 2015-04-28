package it.cosenonjaviste.page;

import dagger.Component;
import it.cosenonjaviste.ApplicationComponent;
import it.cosenonjaviste.lib.mvp.PresenterScope;

@PresenterScope
@Component(dependencies = ApplicationComponent.class)
public interface PageComponent {
    void inject(PageFragment fragment);
}
