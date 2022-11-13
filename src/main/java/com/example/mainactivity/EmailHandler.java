package com.example.mainactivity;

import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;


/**
 * this code is under MIT licence
 * @author MouadOumous
 * @apiNote
 * This class provide tools to filter email input and calculate password strenght
 * to use it call Builder class inside PasswordHandler class and set your costomize filter
 * then create EmailHandler instence by calling build() method inside Builder class, then call
 * addListenerMethod and put your class that implements EmailHandlerListener
 *
 * @see android.text.TextWatcher
 * @see com.example.mainactivity.InputHandler
 *
 */
public class EmailHandler extends InputHandler {


    EmailHandlerListener el;
    private DomainFilter filter;
    private boolean isvalid;
    private boolean has_suported_domain;

    private EmailHandler(EditText client, InputFilter filter) {
        super(client, filter);
        el = null;
        isvalid = false;
        has_suported_domain = false;
    }

    public class Builder {
        private final EditText client;
        private InputFilter filter;

        Builder(EditText client) {
            this.client = client;
        }

        public Builder setFilter(InputFilter bad) {
            this.filter = bad;
            return this;
        }

        public EmailHandler build() {
            return new EmailHandler(client, filter);
        }
    }

    @Override
    public void addListener(InputHandlerListener in) {
        UnsupportedOperationException unsupportedFunction = new UnsupportedOperationException(new Throwable("use addListener(param1) with param1: PasswordHandlerListener"));
        throw unsupportedFunction;
    }

    public void addListener(EmailHandlerListener in) {
        client.addTextChangedListener(this);
        client.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    if (!isvalid)
                        el.invalide_email();
                    if (filter!=null && !has_suported_domain)
                        el.unsuported_domain();
                }
            }
        });
        il = in;
        this.el = in;
    }


    public void setDomainFilter(DomainFilter filter) {
        this.filter = filter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        super.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        super.afterTextChanged(s);
        String email = s.toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isvalid = true;
            if (filter == null)
                return;
            String domain = email.substring(email.indexOf('@'));
            for (String dom : filter.domains) {
                if (domain.equals(dom)) {
                    if (filter.mode!=1)
                        has_suported_domain = false;
                    else
                        has_suported_domain = true;
                    break;
                }
            }
        }
    }

    public interface EmailHandlerListener extends InputHandlerListener {
        public void valide_email();
        public void invalide_email();
        public void unsuported_domain();
    }

    public class DomainFilter implements Filter
    {
        byte mode;
        String[] domains;
        DomainFilter(byte mode)
        {
            this.mode=mode;
        }
        DomainFilter(byte mode,String[] domains)
        {
            this.mode = mode;
            this.domains=domains;
        }
        public DomainFilter setFilter(String[] domains)
        {
            this.domains=domains;
            return this;
        }
    }

}
