package com.example.mainactivity;

import android.text.Editable;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;


/**
 * this code is under MIT licence
 * @author MouadOumous
 * @apiNote
 * This class provide tools to filter password input and calculate password strenght
 * to use it call Builder class inside PasswordHandler class and set your costomize filter
 * then create PasswordHandler instence by calling build() method inside Builder class, then call
 * addListenerMethod and put your class that implements PasswordHandlerListener
 *
 * @see android.text.TextWatcher
 * @see com.example.mainactivity.InputHandler
 *
 */
public class PasswordHandler extends InputHandler{

    public static final byte LEVEL4=60;
    public static final byte LEVEL3=50;
    public static final byte LEVEL2=40;
    public static final byte LEVEL1=30;
    private PasswordHandlerListener pl;
    private final int min_length;
    private byte security_level;
    private PasswordHandler(EditText client, InputFilter filter,int min_length) {
        super(client, filter);
        this.min_length=min_length;
        security_level=LEVEL3;
        pl=null;
    }


    public static class Builder
    {
        private final EditText client;
        InputFilter filter;
        int min_length;
        Builder(EditText client)
        {
            this.client=client;
        }

        public Builder setFilter(InputFilter filter) {
            this.filter = filter;
            return this;
        }
        public Builder setMin_length(int length)
        {
            this.min_length=length;
            return this;
        }
        public PasswordHandler build()
        {
            return  new PasswordHandler(client,filter,min_length);
        }
    }

    @Override
    public void addListener(InputHandlerListener in) {
        UnsupportedOperationException unsupportedFunction = new UnsupportedOperationException(new Throwable("use addListener(param1) with param1: PasswordHandlerListener"));
        throw unsupportedFunction;
    }

    public void addListener(PasswordHandlerListener pl)
    {
        client.addTextChangedListener(this);
        il=pl;
        this.pl=pl;
    }

    private byte get_strength_level(int strength)
    {
        if(strength>=security_level)
            return PasswordHandlerListener.VERY_STRONG;
        else if (strength>=security_level-10)
            return PasswordHandlerListener.STRONG;
        else if (strength>=security_level-20)
            return PasswordHandlerListener.NORMAL;
        else if (strength>=security_level-30)
            return PasswordHandlerListener.WEAK;
        else
            return PasswordHandlerListener.VERY_WEAK;
    }
    public void security_level(byte level)
    {
        security_level=level;
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
        if (!validation_state())
            return;
        if(s.length()<=min_length)
        {
            pl.short_input();
            return;
        }
        Map<Character,Boolean> map = new HashMap<>();
        boolean[] validate = new boolean[4];
        super.afterTextChanged(s);
        for(char c :s.toString().toCharArray())
        {
            if(c>=97 && c<=122)
                validate[0]=true;
            else if(c>=65 && c<=90)
                validate[1]=true;
            else if (c>=48 && c<=57)
                validate[2]=true;
            map.put(c,true);
        }
        int p = 0;
        for(boolean b : validate)
        {
            if(b)
                p++;
        }
        pl.update_strength(get_strength_level(map.size()*p));

    }
    public interface PasswordHandlerListener extends InputHandlerListener {
        byte VERY_STRONG=3;
        byte STRONG=2;
        byte NORMAL=1;
        byte WEAK=0;
        byte VERY_WEAK=-1;
        public void short_input();
        public void update_strength(byte level);
    }

}
