# pltagent-prompt.bashrc

# bash prompt with PASS/FAIL result of last command.
PS1='[$(code=${?##0};echo ${code:+PASS:err=}${code:-FAIL})] \u@\h:\w\$ '
