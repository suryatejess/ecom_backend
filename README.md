# Setup 

# Key Takeways ( by doing this project ) 

Here I will list all the things I learnt by doing this project

- *When generating jwt for users, it is a better idea to generate it based on "userId" instead of username or user email.* 
Why? OAuth users are not going to have username. It is a bad idea to use Oauth user's email as username. Because many oauth users might share the same email in some cases. 
Why not use Email for jwt token creation? For the same reason mentioned above. 
