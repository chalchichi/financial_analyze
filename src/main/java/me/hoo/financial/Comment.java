package me.hoo.financial;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;


@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    private String Company;
    private String Title;
    private String Writer;
    private Integer Views;
    private String Plotpath;
    private String Content;

    public void addview()
    {
        this.Views+=1;
    }
}
