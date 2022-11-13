package com.example.mainactivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * part of my project
 * am open to any opinions
 * this code is under an open source licence MIT
 * @author Mouad Oumous
 * @apiNote
 * java class to filter inputs and handle any
 * unwanted input in EditText
 * this class is working with TextWatcher interface
 *
 */

public class InputHandler implements TextWatcher{
    final EditText client;
    private final InputFilter filter;
    InputHandlerListener il;
    List<Character> unwanted;
    protected InputHandler(EditText client,InputFilter filter) {
        this.client = client;
        this.filter = filter;
        il = null;
        unwanted = new ArrayList<>();
    }


    /**
     * @return boolean
     * @apiNote
     * this method check if the input of this edit text is valid
     * the is valid if the unwanted list is empty
     */
    protected boolean validation_state()
    {
        return unwanted.isEmpty();
    }


    /**
     * we use this class to build InputHandler instence
     */
    public static class Builder {
        EditText client;
        InputFilter filter;

        public Builder(EditText client) {
            this.client = client;
        }

        public Builder setFilter(InputFilter filter) {
            this.filter = filter;
            return this;
        }

        public InputHandler build() {
            return new InputHandler(client, filter);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        char[] chars = s.toString().toCharArray();
        //remove the chars if it exist in unwanted list
        for (int i = start; i < start + count; i++) {
            unwanted.remove((Object) chars[i]);

        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        char[] chars = s.toString().toCharArray();
        for (int i = start; i < start + count; i++) {
            if(filter.mode!=1) {
                for (char c : filter.content) {
                    if (chars[i] == c) {
                        unwanted.add(c);
                        break;
                    }
                }
            }
            else
            {
                byte t=0;
                for (char c : filter.content) {
                    if (chars[i] == c) {
                        t= (byte) c;
                        break;
                    }
                }
                if (t!=0)
                    unwanted.add((char) t);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals(""))
        {
            il.empty_field();
            return;
        }
        if(unwanted.size()!=0)
        {
            il.found_unwanted_char(unwanted.get(unwanted.size()-1));
            return;
        }
        il.valide_input();
    }


    /**
     * @param in {InputHandlerListener}
     */
    public void addListener(InputHandlerListener in) {
        client.addTextChangedListener(this);
        il = in;
    }

    public interface InputHandlerListener {
        public void valide_input();
        public void found_unwanted_char(char c);
        public void empty_field();
    }

    public static class  InputFilter implements Filter
    {
        byte mode;
        char[] content;
        public InputFilter(byte mode,char[] content)
        {
            this.mode=mode;
            this.content=content;
        }
        public InputFilter(byte mode)
        {
            this.mode=mode;
        }
        public InputFilter setContent(char[] f)
        {
            content=f;
            return this;
        }
    }

    public interface Filter {
        byte ACCEPT=1;
        byte DENIE=0;
    }


}
