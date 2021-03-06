{
  "entries":[
    { "name":"Allegheny College",                            "slug":"Allegheny_College",                               "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] ,
      "symbols":[ { "authority":"RESHARE", "symbol":"ACMAIN", "priority":"a" } ,
                  { "authority":"PALCI", "symbol":"ACMAIN", "priority":"a" } ,
                  { "authority":"OCLC",  "symbol":"AVL",    "priority":"a" }
                  ],
      "units": [
        { "name":"Allegheny College Main Library", "slug":"ACMAIN", "tags": ["Branch"],
          "symbols": [ 
                       { "authority":"IDS",   "symbol":"104",    "priority":"a" }
                     ]
        },
        { "name":"Allegheny College Annex Library", "slug":"ACANNEX", "tags": ["Branch"] }
      ],
      "services":[
        { 
          "service":{ "name":"ReShare ISO18626 Service", "address":"http://localhost:8079/iso18626", "type":"ISO18626", "businessFunction":"ILL" },
          "customProperties": { "ILLPreferredNamespaces": [ "PALCI", "IDS" ]  }  
        },
        { 
          "service":{ "name":"ReShare ISO10161 Service", "address":"localhost:499", "type":"ISO10161.TCP", "businessFunction":"ILL" },
          "customProperties": { "ILLPreferredNamespaces": [ "PALCI" ]  }  
        }
      ]
    },
    { "name":"Bloomsburg University",                        "slug":"Bloomsburg_University",                           "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ],
      "units": [
        { "name":"Bloomsburg University Main Library", "slug":"BLOOMMAIN", "tags": ["Branch"] }
      ],
      "symbols":[ 
        { "authority":"PALCI", "symbol":"BLOOMMAIN", "priority":"a" },
        { "authority":"OCLC", "symbol":"PBB", "priority":"a" },
        { "authority":"IDS", "symbol":"109", "priority":"a" } 
      ],
      "services":[
        { "service":{ "address":"https://localhost:8079/iso18626" },
          "customProperties": { "ILLPreferredNamespaces": [ "PALCI", "IDS" ]  } }
      ]
    },
    { "name":"Bryn Mawr College",                            "slug":"Bryn_Mawr_College",                               "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ],
      "symbols":[
        { "authority":"PALCI", "symbol":"BRYN", "priority":"a" },
        { "authority":"OCLC", "symbol":"BMC", "priority":"a" },
        { "authority":"IDS", "symbol":"110", "priority":"a" } 
      ],
      "services":[
        { "service":{ "address":"https://localhost/reshare/iso18626" },
          "customProperties": { "ILLPreferredNamespaces": [ "PALCI", "IDS" ]  } }
      ]
    },
    { "name":"Bucknell University",                          "slug":"Bucknell University",                             "status":"Reference", "tags": [ "Institution" ],
      "symbols":[
        { "authority":"PALCI", "symbol":"BUCK", "priority":"a" },
        { "authority":"OCLC", "symbol":"PBU", "priority":"a" },
        { "authority":"IDS", "symbol":"160", "priority":"a" } 
      ],
      "services":[
        { "service":{ "address":"https://localhost/reshare/iso18626" },
          "customProperties": { "ILLPreferredNamespaces": [ "PALCI", "IDS" ]  } }
      ]
    },
    { "name":"California University of Pennsylvania",        "slug":"California_University_of_Pennsylvania",           "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] ,
      "symbols":[
        { "authority":"PALCI", "symbol":"CUP", "priority":"a" },
        { "authority":"OCLC", "symbol":"CSC", "priority":"a" },
        { "authority":"IDS", "symbol":"112", "priority":"a" } 
      ],
      "services":[
        { "service":{ "address":"https://localhost/reshare/iso18626" },
          "customProperties": { "ILLPreferredNamespaces": [ "PALCI", "IDS" ]  } }
      ]
    },
    { "name":"Carlow University",                            "slug":"Carlow_University",                               "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Carnegie Mellon University",                   "slug":"Carnegie_Mellon_University",                      "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Chatham University",                           "slug":"Chatham_University",                              "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Clarion University",                           "slug":"Clarion_University",                              "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Dickinson College",                            "slug":"Dickinson_College",                               "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Drexel University",                            "slug":"Drexel_University",                               "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Duquesne University",                          "slug":"Duquesne_University",                             "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"East Stroudsburg University",                  "slug":"East_Stroudsburg_University",                     "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Eastern University",                           "slug":"Eastern_University",                              "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Edinboro University",                          "slug":"Edinboro_University",                             "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Elizabethtown College",                        "slug":"Elizabethtown_College",                           "status":"Reference", "tags": [ "Institution" ] },
    { "name":"Franklin & Marshall College",                  "slug":"Franklin_and_Marshall_College",                   "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Gannon University",                            "slug":"Gannon_University",                               "status":"Reference", "tags": [ "Institution" ] },
    { "name":"Gettysburg College",                           "slug":"Gettysburg_College",                              "status":"Reference", "tags": [ "RapidILL", "Institution" ] },
    { "name":"Harrisburg University of Science & Technology","slug":"Harrisburg_University_of_Science_and_Technology", "status":"Reference", "tags": [ "Institution" ]},
    { "name":"Haverford College",                            "slug":"Haverford_College",                               "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Indiana University of Pennsylvania",           "slug":"Indiana_University_of_Pennsylvania",              "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Juniata College",                              "slug":"Juniata_College",                                 "status":"Reference", "tags": [ "Institution" ] },
    { "name":"Kutztown University",                          "slug":"Kutztown_University",                             "status":"Reference", "tags": [ "RapidILL", "Institution" ] },
    { "name":"La Roche College",                             "slug":"La_Roche_College",                                "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"La Salle University",                          "slug":"La_Salle_University",                             "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Lafayette College",                            "slug":"Lafayette_College",                               "status":"Reference", "tags": [ "RapidILL", "Institution" ] },
    { "name":"Lehigh University",                            "slug":"Lehigh_University",                               "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Lock Haven University",                        "slug":"Lock_Haven_University",                           "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Lycoming College",                             "slug":"Lycoming_College",                                "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Mansfield University",                         "slug":"Mansfield_University",                            "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Marshall University",                          "slug":"Marshall_University",                             "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Marywood University",                          "slug":"Marywood_University",                             "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Messiah College",                              "slug":"Messiah_College",                                 "status":"Reference", "tags": [ "Institution" ] },
    { "name":"Millersville University",                      "slug":"Millersville_University",                         "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Misericordia University",                      "slug":"Misericordia_University",                         "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Moravian College",                             "slug":"Moravian_College",                                "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Muhlenberg College",                           "slug":"Muhlenberg_College",                              "status":"Reference", "tags": [ "Institution" ] },
    { "name":"The New School",                               "slug":"The_New_School",                                  "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ],
      "units":[
        { "name":"List Center Library", "tags": ["Branch"], "symbols": { "authority":"RESHARE", "symbol":"TNSLCL", "priority":"a" } },
        { "name":"University Center Library", "tags": ["Branch"], "symbols": { "authority":"RESHARE", "symbol":"TNSUCL", "priority":"a" } },
        { "name":"Performing Arts Library", "tags": ["Branch"], "symbols": { "authority":"RESHARE", "symbol":"TNSPAL", "priority":"a" } }
      ],
      "symbols":[ { "authority":"OCLC", "symbol":"ZMU", "priority":"a" }, { "authority":"RESHARE", "symbol":"TNS", "priority":"a" } ],
      "services":[
        { 
          "service":{ "name":"ReShare ISO18626 Service", "address":"https://localhost/reshare/iso18626", "type":"ISO18626", "businessFunction":"ILL" },
          "customProperties": { "ILLPreferredNamespaces": [ "RESHARE", "PALCI", "IDS" ]  }  
        },
        {
          "service":{ "name":"NCIP", "address":"alephstage.library.nyu.edu:5994", "type":"NCIP", "businessFunction":"CIRC" },
        }
      ]
    },
    { "name":"New York University",                          "slug":"New_York_University",                             "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Pennsylvania State University",                "slug":"Pennsylvania_State_University",                   "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Philadelphia College of Osteopathic Medicine", "slug":"Philadelphia_College_of Osteopathic_Medicine",    "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Philadelphia Museum of Art",                   "slug":"Philadelphia_Museum_of_Art",                      "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Philadelphia University",                      "slug":"Philadelphia_University",                         "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Point Park University",                        "slug":"Point_Park_University",                           "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Robert Morris University",                     "slug":"Robert_Morris_University",                        "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Rowan University",                             "slug":"Rowan_University",                                "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Rutgers University",                           "slug":"Rutgers_University",                              "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Saint Francis University",                     "slug":"Saint_Francis_University",                        "status":"Reference", "tags": [ "Institution" ] },
    { "name":"Saint Joseph's University",                    "slug":"Saint_Josephs_University",                        "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Seton Hall University",                        "slug":"Seton_Hall_University",                           "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Shippensburg University",                      "slug":"Shippensburg_University",                         "status":"Reference", "tags": [ "Institution" ] },
    { "name":"Slippery Rock University",                     "slug":"Slippery_Rock_University",                        "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"State Library of Pennsylvania",                "slug":"State_Library_of_Pennsylvania",                   "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"Susquehanna University",                       "slug":"Susquehanna_University",                          "status":"Reference", "tags": [ "RapidILL", "Institution" ] },
    { "name":"Swarthmore College",                           "slug":"Swarthmore_College",                              "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Temple University",                            "slug":"Temple_University",                               "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"University of Pennsylvania",                   "slug":"University_of_Pennsylvania",                      "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"University of Pittsburgh",                     "slug":"University_of_Pittsburgh",                        "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"University of Scranton",                       "slug":"University_of_Scranton",                          "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"University of the Sciences in Philadelphia",   "slug":"University_of_the_Sciences_in_Philadelphia",      "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Ursinus College",                              "slug":"Ursinus_College",                                 "status":"Reference", "tags": [ "Institution" ] },
    { "name":"Villanova University",                         "slug":"Villanova_University",                            "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Washington & Jefferson College",               "slug":"Washington_and_Jefferson_College",                "status":"Reference", "tags": [ "E-ZBorrow", "Institution" ] },
    { "name":"West Chester University",                      "slug":"West_Chester_University",                         "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"West Virginia University",                     "slug":"West_Virginia_University",                        "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"Widener University",                           "slug":"Widener_University",                              "status":"Reference", "tags": [ "Institution" ],
      "units": [
        { "name":"Widener University Main Library", "tags": ["Branch"], "symbols":[ { "authority":"PALCI", "symbol":"WIDNER", "priority":"a" } ] }
      ]
    },
    { "name":"York College of Pennsylvania",                 "slug":"York_College_of_Pennsylvania",                    "status":"Reference", "tags": [ "E-ZBorrow","RapidILL", "Institution" ] },
    { "name":"PALCI",                                        "slug":"PALCI",                                           "status":"Managed",   "tags": [ "Consortium" ],
      "members":[
        "Allegheny College", "Bloomsburg University"
      ]
    },
    { "name":"Open Library Foundation",                      "slug":"OLF",                                             "status":"Managed",   "tags": [ "Community" ],
      "units":[
        { "name":"North American Office",
          "units": [
            { "name":"Cornell Office", "slug":"OLFHQ" }
          ]
        }
      ]
    },
    { "name":"DIKU",                      "slug":"DIKU",                                             "status":"Managed",   "tags": [ "Institution", "Reshare" ],
      "description": "The standard FOLIO DIKU test library",
      "addresses": [
        {
          "addressLabel":"Index Data Denmark office", "tags":["DefaultAddress","ShippingAddress"], "lines":[
            { "seq":1, "type":"AdministrativeArea", "value":"Univate"},
            { "seq":2, "type":"Thoroughfare", "value":"Njalsgade 76, 13"},
            { "seq":3, "type":"Department", "value":"2300 København S"},
            { "seq":4, "type":"Country", "value":"Denmark"}
          ]
        }
      ],
      "units":[
        { "name":"Branch Library A",
          "symbols": [
                       { "authority":"RESHARE",   "symbol":"DIKUA",    "priority":"a" },
                       { "authority":"PALCI",     "symbol":"DIKUA",    "priority":"a" }
                     ],
          "addresses": [
            {
              "addressLabel":"Index Data Denmark office", "tags":["DefaultAddress","ShippingAddress"], "lines":[
                { "seq":1, "type":"AdministrativeArea", "value":"Univate"},
                { "seq":2, "type":"Thoroughfare", "value":"Njalsgade 76, 13"},
                { "seq":3, "type":"Department", "value":"2300 København S"},
                { "seq":4, "type":"Country", "value":"Denmark"}
              ]
            }
          ]

        },
        { "name":"Branch Library B",
          "symbols": [
                       { "authority":"RESHARE",   "symbol":"DIKUB",    "priority":"a" },
                       { "authority":"PALCI",     "symbol":"DIKUB",    "priority":"a" }
                     ]
        },
        { "name":"Branch Library C",
          "symbols": [
                       { "authority":"RESHARE",   "symbol":"DIKUC",    "priority":"a" },
                       { "authority":"PALCI",     "symbol":"DIKUC",    "priority":"a" }
                     ]
        }
      ],
      "services":[
        { 
          "service":{ "name":"Local ISO18626 Service", "address":"https://localhost:8079/iso18626", "type":"ISO18626", "businessFunction":"ILL" },
          "customProperties": { "ILLPreferredNamespaces": [ "RESHARE" ]  }  
        },
        { 
          "service":{ "name":"ReShare ISO10161 Service", "address":"localhost:499", "type":"ISO10161.TCP", "businessFunction":"ILL" },
          "customProperties": { "ILLPreferredNamespaces": [ "RESHARE" ]  }  
        },
        { 
          "service":{ "name":"ReShare ISO10161 Service", "address":"ill@k-int.com", "type":"GSM.SMTP", "businessFunction":"ILL" },
          "customProperties": { "ILLPreferredNamespaces": [ "RESHARE" ]  }  
        }
      ],
      "customProperties":{
        "demoCustprop": [ "This is a demo custprop on the DIKU test entry" ]
      }
    }
  ]
}
