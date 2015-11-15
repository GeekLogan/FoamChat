# Kernel STDIN/OUT API

| Command               | Uses                                                                                      |
|-----------------------|-------------------------------------------------------------------------------------------|
| lsu                   | Prints all users in the form <ID>:<Display Name>                                          |
| lsm                   | Prints all messages addressed to you in the form <From ID>:<Message>                      |
| lsma                  | Prints all messages, can not be read due to ciphertext <From ID>:<To ID>:<Encrypted Text> |
| msg:<To ID>:<Message> | Sends a new message                                                                       |
| getn-<ID>             | Shows the display name of the user with id <ID>                                           |
