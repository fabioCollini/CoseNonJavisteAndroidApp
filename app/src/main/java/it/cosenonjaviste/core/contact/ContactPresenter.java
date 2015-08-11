package it.cosenonjaviste.core.contact;

import android.databinding.Observable.OnPropertyChangedCallback;
import android.databinding.ObservableInt;

import javax.inject.Inject;

import it.cosenonjaviste.R;
import it.cosenonjaviste.bind.BindableBoolean;
import it.cosenonjaviste.core.model.MailJetService;
import it.cosenonjaviste.core.utils.EmailVerifier;
import it.cosenonjaviste.core.utils.ObservableString;
import it.cosenonjaviste.lib.mvp.RxMvpPresenter;
import retrofit.client.Response;
import rx.Observable;

public class ContactPresenter extends RxMvpPresenter<ContactModel, ContactView> {

    @Inject MailJetService mailJetService;

    public BindableBoolean sending = new BindableBoolean();

    @Inject public ContactPresenter() {
    }

    @Override public ContactModel createDefaultModel() {
        return new ContactModel();
    }

    @Override public void resume() {
        super.resume();

        OnPropertyChangedCallback listener = new OnPropertyChangedCallback() {
            @Override public void onPropertyChanged(android.databinding.Observable sender, int propertyId) {
                validate();
            }
        };
        getModel().name.addOnPropertyChangedCallback(listener);
        getModel().message.addOnPropertyChangedCallback(listener);
        getModel().email.addOnPropertyChangedCallback(listener);
    }

    private boolean validate() {
        ContactModel model = getModel();
        if (model.sendPressed) {
            boolean isValid = checkMandatory(model.name, model.nameError);
            if (!model.email.isEmpty()) {
                if (!EmailVerifier.checkEmail(model.email.get())) {
                    model.emailError.set(R.string.invalid_email);
                    isValid = false;
                } else {
                    model.emailError.set(0);
                }
            } else {
                model.emailError.set(R.string.mandatory_field);
                isValid = false;
            }
            isValid = checkMandatory(model.message, model.messageError) && isValid;
            return isValid;
        } else {
            return true;
        }
    }

    private boolean checkMandatory(ObservableString bindableString, ObservableInt error) {
        boolean empty = bindableString.isEmpty();
        error.set(empty ? R.string.mandatory_field : 0);
        return !empty;
    }

    public void send() {
        ContactModel model = getModel();
        model.sendPressed = true;
        if (validate()) {
            sending.set(true);

            Observable<Response> observable = mailJetService.sendEmail(
                    model.name + " <info@cosenonjaviste.it>",
                    "info@cosenonjaviste.it",
                    "Email from " + model.name,
                    "Reply to: " + model.email + "\n" + model.message
            ).finallyDo(() -> sending.set(false));

            subscribe(
                    observable,
                    r -> getView().showSentMessage(),
                    t -> getView().showSentError()
            );
        }
    }

    public ObservableString getName() {
        return getModel().name;
    }

    public ObservableString getEmail() {
        return getModel().email;
    }

    public ObservableString getMessage() {
        return getModel().message;
    }

    public ObservableInt getNameError() {
        return getModel().nameError;
    }

    public ObservableInt getEmailError() {
        return getModel().emailError;
    }

    public ObservableInt getMessageError() {
        return getModel().messageError;
    }
}
